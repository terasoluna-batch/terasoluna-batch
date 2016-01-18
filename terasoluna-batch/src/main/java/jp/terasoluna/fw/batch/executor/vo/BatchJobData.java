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

import org.kohsuke.args4j.Argument;

import java.sql.Timestamp;

/**
 * 実行対象ジョブの情報。<br>
 */
public class BatchJobData {
    /**
     * フィールド [jobSequenceId] 項目の型 [java.lang.String]<br>
     * ジョブシーケンスコード
     */
    private String jobSequenceId;

    /**
     * フィールド [jobAppCd] 項目の型 [java.lang.String]<br>
     * ジョブ業務コード
     */
    @Argument(index = 0)
    private String fJobAppCd;

    /**
     * フィールド [jobArgNm1] 項目の型 [java.lang.String]<br>
     * 引数1
     */
    @Argument(index = 1)
    private String fJobArgNm1;

    /**
     * フィールド [jobArgNm2] 項目の型 [java.lang.String]<br>
     * 引数2
     */
    @Argument(index = 2)
    private String fJobArgNm2;

    /**
     * フィールド [jobArgNm3] 項目の型 [java.lang.String]<br>
     * 引数3
     */
    @Argument(index = 3)
    private String fJobArgNm3;

    /**
     * フィールド [jobArgNm4] 項目の型 [java.lang.String]<br>
     * 引数4
     */
    @Argument(index = 4)
    private String fJobArgNm4;

    /**
     * フィールド [jobArgNm5] 項目の型 [java.lang.String]<br>
     * 引数5
     */
    @Argument(index = 5)
    private String fJobArgNm5;

    /**
     * フィールド [jobArgNm6] 項目の型 [java.lang.String]<br>
     * 引数6
     */
    @Argument(index = 6)
    private String fJobArgNm6;

    /**
     * フィールド [jobArgNm7] 項目の型 [java.lang.String]<br>
     * 引数7
     */
    @Argument(index = 7)
    private String fJobArgNm7;

    /**
     * フィールド [jobArgNm8] 項目の型 [java.lang.String]<br>
     * 引数8
     */
    @Argument(index = 8)
    private String fJobArgNm8;

    /**
     * フィールド [jobArgNm9] 項目の型 [java.lang.String]<br>
     * 引数9
     */
    @Argument(index = 9)
    private String fJobArgNm9;

    /**
     * フィールド [jobArgNm10] 項目の型 [java.lang.String]<br>
     * 引数10
     */
    @Argument(index = 10)
    private String fJobArgNm10;

    /**
     * フィールド [jobArgNm11] 項目の型 [java.lang.String]<br>
     * 引数11
     */
    @Argument(index = 11)
    private String fJobArgNm11;

    /**
     * フィールド [jobArgNm12] 項目の型 [java.lang.String]<br>
     * 引数12
     */
    @Argument(index = 12)
    private String fJobArgNm12;

    /**
     * フィールド [jobArgNm13] 項目の型 [java.lang.String]<br>
     * 引数13
     */
    @Argument(index = 13)
    private String fJobArgNm13;

    /**
     * フィールド [jobArgNm14] 項目の型 [java.lang.String]<br>
     * 引数14
     */
    @Argument(index = 14)
    private String fJobArgNm14;

    /**
     * フィールド [jobArgNm15] 項目の型 [java.lang.String]<br>
     * 引数15
     */
    @Argument(index = 15)
    private String fJobArgNm15;

    /**
     * フィールド [jobArgNm16] 項目の型 [java.lang.String]<br>
     * 引数16
     */
    @Argument(index = 16)
    private String fJobArgNm16;

    /**
     * フィールド [jobArgNm17] 項目の型 [java.lang.String]<br>
     * 引数17
     */
    @Argument(index = 17)
    private String fJobArgNm17;

    /**
     * フィールド [jobArgNm18] 項目の型 [java.lang.String]<br>
     * 引数18
     */
    @Argument(index = 18)
    private String fJobArgNm18;

    /**
     * フィールド [jobArgNm19] 項目の型 [java.lang.String]<br>
     * 引数19
     */
    @Argument(index = 19)
    private String fJobArgNm19;

    /**
     * フィールド [jobArgNm20] 項目の型 [java.lang.String]<br>
     * 引数20
     */
    @Argument(index = 20)
    private String fJobArgNm20;

    /**
     * フィールド [blogicAppStatus] 項目の型 [java.lang.String]<br>
     * BLogicステータス
     */
    private String fBLogicAppStatus;

    /**
     * フィールド [curAppStatus] 項目の型 [java.lang.String]<br>
     * ステータス
     */
    private String fCurAppStatus;

    /**
     * フィールド [addDateTime] 項目の型 [java.sql.Timestamp]<br>
     * 登録日時（ミリ秒）
     */
    private Timestamp fAddDateTime;

    /**
     * フィールド [updDateTime] 項目の型 [java.sql.Timestamp]<br>
     * 更新日時（ミリ秒）
     */
    private Timestamp fUpdDateTime;

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
     * フィールド [jobAppCd]のセッターメソッド 項目の型 [java.lang.String]<br>
     * ジョブ業務コード
     * @param argJobAppCd フィールド[jobAppCd]に格納したい値
     */
    public void setJobAppCd(final String argJobAppCd) {
        fJobAppCd = argJobAppCd;
    }

    /**
     * フィールド[jobAppCd]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * ジョブ業務コード
     * @return フィールド[jobAppCd]に格納されている値
     */
    public String getJobAppCd() {
        return fJobAppCd;
    }

    /**
     * フィールド [jobArgNm1]のセッターメソッド 項目の型 [java.lang.String]<br>
     * 引数1
     * @param argJobArgNm1 フィールド[jobArgNm1]に格納したい値
     */
    public void setJobArgNm1(final String argJobArgNm1) {
        fJobArgNm1 = argJobArgNm1;
    }

    /**
     * フィールド[jobArgNm1]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * 引数1
     * @return フィールド[jobArgNm1]に格納されている値
     */
    public String getJobArgNm1() {
        return fJobArgNm1;
    }

    /**
     * フィールド [jobArgNm2]のセッターメソッド 項目の型 [java.lang.String]<br>
     * 引数2
     * @param argJobArgNm2 フィールド[jobArgNm2]に格納したい値
     */
    public void setJobArgNm2(final String argJobArgNm2) {
        fJobArgNm2 = argJobArgNm2;
    }

    /**
     * フィールド[jobArgNm2]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * 引数2
     * @return フィールド[jobArgNm2]に格納されている値
     */
    public String getJobArgNm2() {
        return fJobArgNm2;
    }

    /**
     * フィールド [jobArgNm3]のセッターメソッド 項目の型 [java.lang.String]<br>
     * 引数3
     * @param argJobArgNm3 フィールド[jobArgNm3]に格納したい値
     */
    public void setJobArgNm3(final String argJobArgNm3) {
        fJobArgNm3 = argJobArgNm3;
    }

    /**
     * フィールド[jobArgNm3]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * 引数3
     * @return フィールド[jobArgNm3]に格納されている値
     */
    public String getJobArgNm3() {
        return fJobArgNm3;
    }

    /**
     * フィールド [jobArgNm4]のセッターメソッド 項目の型 [java.lang.String]<br>
     * 引数4
     * @param argJobArgNm4 フィールド[jobArgNm4]に格納したい値
     */
    public void setJobArgNm4(final String argJobArgNm4) {
        fJobArgNm4 = argJobArgNm4;
    }

    /**
     * フィールド[jobArgNm4]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * 引数4
     * @return フィールド[jobArgNm4]に格納されている値
     */
    public String getJobArgNm4() {
        return fJobArgNm4;
    }

    /**
     * フィールド [jobArgNm5]のセッターメソッド 項目の型 [java.lang.String]<br>
     * 引数5
     * @param argJobArgNm5 フィールド[jobArgNm5]に格納したい値
     */
    public void setJobArgNm5(final String argJobArgNm5) {
        fJobArgNm5 = argJobArgNm5;
    }

    /**
     * フィールド[jobArgNm5]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * 引数5
     * @return フィールド[jobArgNm5]に格納されている値
     */
    public String getJobArgNm5() {
        return fJobArgNm5;
    }

    /**
     * フィールド [jobArgNm6]のセッターメソッド 項目の型 [java.lang.String]<br>
     * 引数6
     * @param argJobArgNm6 フィールド[jobArgNm6]に格納したい値
     */
    public void setJobArgNm6(final String argJobArgNm6) {
        fJobArgNm6 = argJobArgNm6;
    }

    /**
     * フィールド[jobArgNm6]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * 引数6
     * @return フィールド[jobArgNm6]に格納されている値
     */
    public String getJobArgNm6() {
        return fJobArgNm6;
    }

    /**
     * フィールド [jobArgNm7]のセッターメソッド 項目の型 [java.lang.String]<br>
     * 引数7
     * @param argJobArgNm7 フィールド[jobArgNm7]に格納したい値
     */
    public void setJobArgNm7(final String argJobArgNm7) {
        fJobArgNm7 = argJobArgNm7;
    }

    /**
     * フィールド[jobArgNm7]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * 引数7
     * @return フィールド[jobArgNm7]に格納されている値
     */
    public String getJobArgNm7() {
        return fJobArgNm7;
    }

    /**
     * フィールド [jobArgNm8]のセッターメソッド 項目の型 [java.lang.String]<br>
     * 引数8
     * @param argJobArgNm8 フィールド[jobArgNm8]に格納したい値
     */
    public void setJobArgNm8(final String argJobArgNm8) {
        fJobArgNm8 = argJobArgNm8;
    }

    /**
     * フィールド[jobArgNm8]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * 引数8
     * @return フィールド[jobArgNm8]に格納されている値
     */
    public String getJobArgNm8() {
        return fJobArgNm8;
    }

    /**
     * フィールド [jobArgNm9]のセッターメソッド 項目の型 [java.lang.String]<br>
     * 引数9
     * @param argJobArgNm9 フィールド[jobArgNm9]に格納したい値
     */
    public void setJobArgNm9(final String argJobArgNm9) {
        fJobArgNm9 = argJobArgNm9;
    }

    /**
     * フィールド[jobArgNm9]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * 引数9
     * @return フィールド[jobArgNm9]に格納されている値
     */
    public String getJobArgNm9() {
        return fJobArgNm9;
    }

    /**
     * フィールド [jobArgNm10]のセッターメソッド 項目の型 [java.lang.String]<br>
     * 引数10
     * @param argJobArgNm10 フィールド[jobArgNm10]に格納したい値
     */
    public void setJobArgNm10(final String argJobArgNm10) {
        fJobArgNm10 = argJobArgNm10;
    }

    /**
     * フィールド[jobArgNm10]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * 引数10
     * @return フィールド[jobArgNm10]に格納されている値
     */
    public String getJobArgNm10() {
        return fJobArgNm10;
    }

    /**
     * フィールド [jobArgNm11]のセッターメソッド 項目の型 [java.lang.String]<br>
     * 引数11
     * @param argJobArgNm11 フィールド[jobArgNm11]に格納したい値
     */
    public void setJobArgNm11(final String argJobArgNm11) {
        fJobArgNm11 = argJobArgNm11;
    }

    /**
     * フィールド[jobArgNm11]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * 引数11
     * @return フィールド[jobArgNm11]に格納されている値
     */
    public String getJobArgNm11() {
        return fJobArgNm11;
    }

    /**
     * フィールド [jobArgNm12]のセッターメソッド 項目の型 [java.lang.String]<br>
     * 引数12
     * @param argJobArgNm12 フィールド[jobArgNm12]に格納したい値
     */
    public void setJobArgNm12(final String argJobArgNm12) {
        fJobArgNm12 = argJobArgNm12;
    }

    /**
     * フィールド[jobArgNm12]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * 引数12
     * @return フィールド[jobArgNm12]に格納されている値
     */
    public String getJobArgNm12() {
        return fJobArgNm12;
    }

    /**
     * フィールド [jobArgNm13]のセッターメソッド 項目の型 [java.lang.String]<br>
     * 引数13
     * @param argJobArgNm13 フィールド[jobArgNm13]に格納したい値
     */
    public void setJobArgNm13(final String argJobArgNm13) {
        fJobArgNm13 = argJobArgNm13;
    }

    /**
     * フィールド[jobArgNm13]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * 引数13
     * @return フィールド[jobArgNm13]に格納されている値
     */
    public String getJobArgNm13() {
        return fJobArgNm13;
    }

    /**
     * フィールド [jobArgNm14]のセッターメソッド 項目の型 [java.lang.String]<br>
     * 引数14
     * @param argJobArgNm14 フィールド[jobArgNm14]に格納したい値
     */
    public void setJobArgNm14(final String argJobArgNm14) {
        fJobArgNm14 = argJobArgNm14;
    }

    /**
     * フィールド[jobArgNm14]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * 引数14
     * @return フィールド[jobArgNm14]に格納されている値
     */
    public String getJobArgNm14() {
        return fJobArgNm14;
    }

    /**
     * フィールド [jobArgNm15]のセッターメソッド 項目の型 [java.lang.String]<br>
     * 引数15
     * @param argJobArgNm15 フィールド[jobArgNm15]に格納したい値
     */
    public void setJobArgNm15(final String argJobArgNm15) {
        fJobArgNm15 = argJobArgNm15;
    }

    /**
     * フィールド[jobArgNm15]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * 引数15
     * @return フィールド[jobArgNm15]に格納されている値
     */
    public String getJobArgNm15() {
        return fJobArgNm15;
    }

    /**
     * フィールド [jobArgNm16]のセッターメソッド 項目の型 [java.lang.String]<br>
     * 引数16
     * @param argJobArgNm16 フィールド[jobArgNm16]に格納したい値
     */
    public void setJobArgNm16(final String argJobArgNm16) {
        fJobArgNm16 = argJobArgNm16;
    }

    /**
     * フィールド[jobArgNm16]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * 引数16
     * @return フィールド[jobArgNm16]に格納されている値
     */
    public String getJobArgNm16() {
        return fJobArgNm16;
    }

    /**
     * フィールド [jobArgNm17]のセッターメソッド 項目の型 [java.lang.String]<br>
     * 引数17
     * @param argJobArgNm17 フィールド[jobArgNm17]に格納したい値
     */
    public void setJobArgNm17(final String argJobArgNm17) {
        fJobArgNm17 = argJobArgNm17;
    }

    /**
     * フィールド[jobArgNm17]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * 引数17
     * @return フィールド[jobArgNm17]に格納されている値
     */
    public String getJobArgNm17() {
        return fJobArgNm17;
    }

    /**
     * フィールド [jobArgNm18]のセッターメソッド 項目の型 [java.lang.String]<br>
     * 引数18
     * @param argJobArgNm18 フィールド[jobArgNm18]に格納したい値
     */
    public void setJobArgNm18(final String argJobArgNm18) {
        fJobArgNm18 = argJobArgNm18;
    }

    /**
     * フィールド[jobArgNm18]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * 引数18
     * @return フィールド[jobArgNm18]に格納されている値
     */
    public String getJobArgNm18() {
        return fJobArgNm18;
    }

    /**
     * フィールド [jobArgNm19]のセッターメソッド 項目の型 [java.lang.String]<br>
     * 引数19
     * @param argJobArgNm19 フィールド[jobArgNm19]に格納したい値
     */
    public void setJobArgNm19(final String argJobArgNm19) {
        fJobArgNm19 = argJobArgNm19;
    }

    /**
     * フィールド[jobArgNm19]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * 引数19
     * @return フィールド[jobArgNm19]に格納されている値
     */
    public String getJobArgNm19() {
        return fJobArgNm19;
    }

    /**
     * フィールド [jobArgNm20]のセッターメソッド 項目の型 [java.lang.String]<br>
     * 引数20
     * @param argJobArgNm20 フィールド[jobArgNm20]に格納したい値
     */
    public void setJobArgNm20(final String argJobArgNm20) {
        fJobArgNm20 = argJobArgNm20;
    }

    /**
     * フィールド[jobArgNm20]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * 引数20
     * @return フィールド[jobArgNm20]に格納されている値
     */
    public String getJobArgNm20() {
        return fJobArgNm20;
    }

    /**
     * フィールド [blogicAppStatus]のセッターメソッド 項目の型 [java.lang.String]<br>
     * BLogicステータス
     * @param argBLogicAppStatus フィールド[blogicAppStatus]に格納したい値
     */
    public void setErrAppStatus(final String argBLogicAppStatus) {
        fBLogicAppStatus = argBLogicAppStatus;
    }

    /**
     * フィールド[blogicAppStatus]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * BLogicステータス
     * @return フィールド[blogicAppStatus]に格納されている値
     */
    public String getBLogicAppStatus() {
        return fBLogicAppStatus;
    }

    /**
     * フィールド [curAppStatus]のセッターメソッド 項目の型 [java.lang.String]<br>
     * ステータス
     * @param argCurAppStatus フィールド[curAppStatus]に格納したい値
     */
    public void setCurAppStatus(final String argCurAppStatus) {
        fCurAppStatus = argCurAppStatus;
    }

    /**
     * フィールド[curAppStatus]のゲッターメソッド 項目の型 [java.lang.String]<br>
     * ステータス
     * @return フィールド[curAppStatus]に格納されている値
     */
    public String getCurAppStatus() {
        return fCurAppStatus;
    }

    /**
     * フィールド [addDateTime]のセッターメソッド 項目の型 [java.sql.Timestamp]<br>
     * 登録日時（ミリ秒）
     * @param argAddDateTime フィールド[addDateTime]に格納したい値
     */
    public void setAddDateTime(final Timestamp argAddDateTime) {
        fAddDateTime = argAddDateTime;
    }

    /**
     * フィールド[addDateTime]のゲッターメソッド 項目の型 [java.sql.Timestamp]<br>
     * 登録日時（ミリ秒）
     * @return フィールド[addDateTime]に格納されている値
     */
    public Timestamp getAddDateTime() {
        return fAddDateTime;
    }

    /**
     * フィールド [updDateTime]のセッターメソッド 項目の型 [java.sql.Timestamp]<br>
     * 更新日時（ミリ秒）
     * @param argUpdDateTime フィールド[updDateTime]に格納したい値
     */
    public void setUpdDateTime(final Timestamp argUpdDateTime) {
        fUpdDateTime = argUpdDateTime;
    }

    /**
     * フィールド[updDateTime]のゲッターメソッド 項目の型 [java.sql.Timestamp]<br>
     * 更新日時（ミリ秒）
     * @return フィールド[updDateTime]に格納されている値
     */
    public Timestamp getUpdDateTime() {
        return fUpdDateTime;
    }

    /**
     * このバリューオブジェクトの文字列表現を取得します。 オブジェクトのシャロー範囲でしかtoStringされない点に注意して利用してください。
     * @return バリューオブジェクトの文字列表現。
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BatchJobData[");
        sb.append("jobSequenceId=" + jobSequenceId);
        sb.append(",jobAppCd=" + fJobAppCd);
        sb.append(",jobArgNm1=" + fJobArgNm1);
        sb.append(",jobArgNm2=" + fJobArgNm2);
        sb.append(",jobArgNm3=" + fJobArgNm3);
        sb.append(",jobArgNm4=" + fJobArgNm4);
        sb.append(",jobArgNm5=" + fJobArgNm5);
        sb.append(",jobArgNm6=" + fJobArgNm6);
        sb.append(",jobArgNm7=" + fJobArgNm7);
        sb.append(",jobArgNm8=" + fJobArgNm8);
        sb.append(",jobArgNm9=" + fJobArgNm9);
        sb.append(",jobArgNm10=" + fJobArgNm10);
        sb.append(",jobArgNm11=" + fJobArgNm11);
        sb.append(",jobArgNm12=" + fJobArgNm12);
        sb.append(",jobArgNm13=" + fJobArgNm13);
        sb.append(",jobArgNm14=" + fJobArgNm14);
        sb.append(",jobArgNm15=" + fJobArgNm15);
        sb.append(",jobArgNm16=" + fJobArgNm16);
        sb.append(",jobArgNm17=" + fJobArgNm17);
        sb.append(",jobArgNm18=" + fJobArgNm18);
        sb.append(",jobArgNm19=" + fJobArgNm19);
        sb.append(",jobArgNm20=" + fJobArgNm20);
        sb.append(",blogicAppStatus=" + fBLogicAppStatus);
        sb.append(",curAppStatus=" + fCurAppStatus);
        sb.append(",addDateTime=" + fAddDateTime);
        sb.append(",updDateTime=" + fUpdDateTime);
        sb.append("]");
        return sb.toString();
    }
}
