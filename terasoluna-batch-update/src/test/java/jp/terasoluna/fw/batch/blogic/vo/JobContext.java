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

package jp.terasoluna.fw.batch.blogic.vo;

import java.io.Serializable;

import jp.terasoluna.fw.batch.dao.support.BatchUpdateSupport;
import jp.terasoluna.fw.batch.dao.support.BatchUpdateSupportImpl;

public class JobContext extends BLogicParam implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 2544252352103871021L;

    /** ビジネスロジックの入力パラメータ */
    protected BLogicParam blogicParam = null;

    /** バッチ更新サポート */
    protected BatchUpdateSupport batchUpdateSupport = null;

    /**
     * コンストラクタ.<br>
     */
    public JobContext() {
        this.blogicParam = new BLogicParam();
        this.batchUpdateSupport = new BatchUpdateSupportImpl();
    }

    /**
     * コンストラクタ.<br>
     * @param blogicParam BLogicParam
     */
    public JobContext(BLogicParam blogicParam) {
        this.blogicParam = blogicParam;
        this.batchUpdateSupport = new BatchUpdateSupportImpl();
    }

    /**
     * ビジネスロジックの入力パラメータを設定する。
     * @param blogicParam BLogicParam
     */
    public void setBlogicParam(BLogicParam blogicParam) {
        this.blogicParam = blogicParam;
    }

    /**
     * フィールド [jobSequenceId]のセッターメソッド. 項目の型 [java.lang.String]<br>
     * ジョブシーケンスコード
     * @param jobSequenceId フィールド[jobSequenceId]に格納したい値
     */
    public void setJobSequenceId(final String jobSequenceId) {
        this.blogicParam.setJobSequenceId(jobSequenceId);
    }

    /**
     * フィールド[jobSequenceId]のゲッターメソッド. 項目の型 [java.lang.String]<br>
     * ジョブシーケンスコード
     * @return フィールド[jobSequenceId]に格納されている値
     */
    public String getJobSequenceId() {
        return this.blogicParam.getJobSequenceId();
    }

    /**
     * フィールド [jobAppCd]のセッターメソッド. 項目の型 [java.lang.String]<br>
     * ジョブシーケンスコード
     * @param argJobAppCd フィールド[jobAppCd]に格納したい値
     */
    public void setJobAppCd(String jobAppCd) {
        this.blogicParam.setJobAppCd(jobAppCd);
    }

    /**
     * フィールド[fJobAppCd]のゲッターメソッド. 項目の型 [java.lang.String]<br>
     * ジョブ業務コード
     * @return フィールド[fJobAppCd]に格納されている値
     */
    public String getJobAppCd() {
        return this.blogicParam.getJobAppCd();
    }

    /**
     * フィールド [jobArgNm1]のセッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数1
     * @param argJobArgNm1 フィールド[jobArgNm1]に格納したい値
     */
    public void setJobArgNm1(final String argJobArgNm1) {
        this.blogicParam.setJobArgNm1(argJobArgNm1);
    }

    /**
     * フィールド[jobArgNm1]のゲッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数1
     * @return フィールド[jobArgNm1]に格納されている値
     */
    public String getJobArgNm1() {
        return this.blogicParam.getJobArgNm1();
    }

    /**
     * フィールド [jobArgNm2]のセッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数2
     * @param argJobArgNm2 フィールド[jobArgNm2]に格納したい値
     */
    public void setJobArgNm2(final String argJobArgNm2) {
        this.blogicParam.setJobArgNm2(argJobArgNm2);
    }

    /**
     * フィールド[jobArgNm2]のゲッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数2
     * @return フィールド[jobArgNm2]に格納されている値
     */
    public String getJobArgNm2() {
        return this.blogicParam.getJobArgNm2();
    }

    /**
     * フィールド [jobArgNm3]のセッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数3
     * @param argJobArgNm3 フィールド[jobArgNm3]に格納したい値
     */
    public void setJobArgNm3(final String argJobArgNm3) {
        this.blogicParam.setJobArgNm3(argJobArgNm3);
    }

    /**
     * フィールド[jobArgNm3]のゲッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数3
     * @return フィールド[jobArgNm3]に格納されている値
     */
    public String getJobArgNm3() {
        return this.blogicParam.getJobArgNm3();
    }

    /**
     * フィールド [jobArgNm4]のセッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数4
     * @param argJobArgNm4 フィールド[jobArgNm4]に格納したい値
     */
    public void setJobArgNm4(final String argJobArgNm4) {
        this.blogicParam.setJobArgNm4(argJobArgNm4);
    }

    /**
     * フィールド[jobArgNm4]のゲッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数4
     * @return フィールド[jobArgNm4]に格納されている値
     */
    public String getJobArgNm4() {
        return this.blogicParam.getJobArgNm4();
    }

    /**
     * フィールド [jobArgNm5]のセッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数5
     * @param argJobArgNm5 フィールド[jobArgNm5]に格納したい値
     */
    public void setJobArgNm5(final String argJobArgNm5) {
        this.blogicParam.setJobArgNm5(argJobArgNm5);
    }

    /**
     * フィールド[jobArgNm5]のゲッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数5
     * @return フィールド[jobArgNm5]に格納されている値
     */
    public String getJobArgNm5() {
        return this.blogicParam.getJobArgNm5();
    }

    /**
     * フィールド [jobArgNm6]のセッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数6
     * @param argJobArgNm6 フィールド[jobArgNm6]に格納したい値
     */
    public void setJobArgNm6(final String argJobArgNm6) {
        this.blogicParam.setJobArgNm6(argJobArgNm6);
    }

    /**
     * フィールド[jobArgNm6]のゲッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数6
     * @return フィールド[jobArgNm6]に格納されている値
     */
    public String getJobArgNm6() {
        return this.blogicParam.getJobArgNm6();
    }

    /**
     * フィールド [jobArgNm7]のセッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数7
     * @param argJobArgNm7 フィールド[jobArgNm7]に格納したい値
     */
    public void setJobArgNm7(final String argJobArgNm7) {
        this.blogicParam.setJobArgNm7(argJobArgNm7);
    }

    /**
     * フィールド[jobArgNm7]のゲッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数7
     * @return フィールド[jobArgNm7]に格納されている値
     */
    public String getJobArgNm7() {
        return this.blogicParam.getJobArgNm7();
    }

    /**
     * フィールド [jobArgNm8]のセッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数8
     * @param argJobArgNm8 フィールド[jobArgNm8]に格納したい値
     */
    public void setJobArgNm8(final String argJobArgNm8) {
        this.blogicParam.setJobArgNm8(argJobArgNm8);
    }

    /**
     * フィールド[jobArgNm8]のゲッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数8
     * @return フィールド[jobArgNm8]に格納されている値
     */
    public String getJobArgNm8() {
        return this.blogicParam.getJobArgNm8();
    }

    /**
     * フィールド [jobArgNm9]のセッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数9
     * @param argJobArgNm9 フィールド[jobArgNm9]に格納したい値
     */
    public void setJobArgNm9(final String argJobArgNm9) {
        this.blogicParam.setJobArgNm9(argJobArgNm9);
    }

    /**
     * フィールド[jobArgNm9]のゲッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数9
     * @return フィールド[jobArgNm9]に格納されている値
     */
    public String getJobArgNm9() {
        return this.blogicParam.getJobArgNm9();
    }

    /**
     * フィールド [jobArgNm10]のセッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数10
     * @param argJobArgNm10 フィールド[jobArgNm10]に格納したい値
     */
    public void setJobArgNm10(final String argJobArgNm10) {
        this.blogicParam.setJobArgNm10(argJobArgNm10);
    }

    /**
     * フィールド[jobArgNm10]のゲッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数10
     * @return フィールド[jobArgNm10]に格納されている値
     */
    public String getJobArgNm10() {
        return this.blogicParam.getJobArgNm10();
    }

    /**
     * フィールド [jobArgNm11]のセッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数11
     * @param argJobArgNm11 フィールド[jobArgNm11]に格納したい値
     */
    public void setJobArgNm11(final String argJobArgNm11) {
        this.blogicParam.setJobArgNm11(argJobArgNm11);
    }

    /**
     * フィールド[jobArgNm11]のゲッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数11
     * @return フィールド[jobArgNm11]に格納されている値
     */
    public String getJobArgNm11() {
        return this.blogicParam.getJobArgNm11();
    }

    /**
     * フィールド [jobArgNm12]のセッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数12
     * @param argJobArgNm12 フィールド[jobArgNm12]に格納したい値
     */
    public void setJobArgNm12(final String argJobArgNm12) {
        this.blogicParam.setJobArgNm12(argJobArgNm12);
    }

    /**
     * フィールド[jobArgNm12]のゲッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数12
     * @return フィールド[jobArgNm12]に格納されている値
     */
    public String getJobArgNm12() {
        return this.blogicParam.getJobArgNm12();
    }

    /**
     * フィールド [jobArgNm13]のセッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数13
     * @param argJobArgNm13 フィールド[jobArgNm13]に格納したい値
     */
    public void setJobArgNm13(final String argJobArgNm13) {
        this.blogicParam.setJobArgNm13(argJobArgNm13);
    }

    /**
     * フィールド[jobArgNm13]のゲッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数13
     * @return フィールド[jobArgNm13]に格納されている値
     */
    public String getJobArgNm13() {
        return this.blogicParam.getJobArgNm13();
    }

    /**
     * フィールド [jobArgNm14]のセッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数14
     * @param argJobArgNm14 フィールド[jobArgNm14]に格納したい値
     */
    public void setJobArgNm14(final String argJobArgNm14) {
        this.blogicParam.setJobArgNm14(argJobArgNm14);
    }

    /**
     * フィールド[jobArgNm14]のゲッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数14
     * @return フィールド[jobArgNm14]に格納されている値
     */
    public String getJobArgNm14() {
        return this.blogicParam.getJobArgNm14();
    }

    /**
     * フィールド [jobArgNm15]のセッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数15
     * @param argJobArgNm15 フィールド[jobArgNm15]に格納したい値
     */
    public void setJobArgNm15(final String argJobArgNm15) {
        this.blogicParam.setJobArgNm15(argJobArgNm15);
    }

    /**
     * フィールド[jobArgNm15]のゲッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数15
     * @return フィールド[jobArgNm15]に格納されている値
     */
    public String getJobArgNm15() {
        return this.blogicParam.getJobArgNm15();
    }

    /**
     * フィールド [jobArgNm16]のセッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数16
     * @param argJobArgNm16 フィールド[jobArgNm16]に格納したい値
     */
    public void setJobArgNm16(final String argJobArgNm16) {
        this.blogicParam.setJobArgNm16(argJobArgNm16);
    }

    /**
     * フィールド[jobArgNm16]のゲッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数16
     * @return フィールド[jobArgNm16]に格納されている値
     */
    public String getJobArgNm16() {
        return this.blogicParam.getJobArgNm16();
    }

    /**
     * フィールド [jobArgNm17]のセッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数17
     * @param argJobArgNm17 フィールド[jobArgNm17]に格納したい値
     */
    public void setJobArgNm17(final String argJobArgNm17) {
        this.blogicParam.setJobArgNm17(argJobArgNm17);
    }

    /**
     * フィールド[jobArgNm17]のゲッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数17
     * @return フィールド[jobArgNm17]に格納されている値
     */
    public String getJobArgNm17() {
        return this.blogicParam.getJobArgNm17();
    }

    /**
     * フィールド [jobArgNm18]のセッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数18
     * @param argJobArgNm18 フィールド[jobArgNm18]に格納したい値
     */
    public void setJobArgNm18(final String argJobArgNm18) {
        this.blogicParam.setJobArgNm18(argJobArgNm18);
    }

    /**
     * フィールド[jobArgNm18]のゲッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数18
     * @return フィールド[jobArgNm18]に格納されている値
     */
    public String getJobArgNm18() {
        return this.blogicParam.getJobArgNm18();
    }

    /**
     * フィールド [jobArgNm19]のセッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数19
     * @param argJobArgNm19 フィールド[jobArgNm19]に格納したい値
     */
    public void setJobArgNm19(final String argJobArgNm19) {
        this.blogicParam.setJobArgNm19(argJobArgNm19);
    }

    /**
     * フィールド[jobArgNm19]のゲッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数19
     * @return フィールド[jobArgNm19]に格納されている値
     */
    public String getJobArgNm19() {
        return this.blogicParam.getJobArgNm19();
    }

    /**
     * フィールド [jobArgNm20]のセッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数20
     * @param argJobArgNm20 フィールド[jobArgNm20]に格納したい値
     */
    public void setJobArgNm20(final String argJobArgNm20) {
        this.blogicParam.setJobArgNm20(argJobArgNm20);
    }

    /**
     * フィールド[jobArgNm20]のゲッターメソッド. 項目の型 [java.lang.String]<br>
     * 引数20
     * @return フィールド[jobArgNm20]に格納されている値
     */
    public String getJobArgNm20() {
        return this.blogicParam.getJobArgNm20();
    }

    /**
     * バッチ更新サポートを取得する.<br>
     * @return BatchUpdateSupport
     */
    public BatchUpdateSupport getBatchUpdateSupport() {
        return batchUpdateSupport;
    }

    /**
     * バッチ更新サポートを設定する.<br>
     * @param batchUpdateSupport BatchUpdateSupport
     */
    public void setBatchUpdateSupport(BatchUpdateSupport batchUpdateSupport) {
        this.batchUpdateSupport = batchUpdateSupport;
    }

}
