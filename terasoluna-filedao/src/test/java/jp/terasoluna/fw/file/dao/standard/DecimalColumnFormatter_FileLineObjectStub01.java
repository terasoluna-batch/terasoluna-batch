package jp.terasoluna.fw.file.dao.standard;

import java.math.BigDecimal;

/**
 * DecimalColumnFormatterの試験で利用するファイル行オブジェクトクラス。<br>
 * 各試験別にフィールドを分けている。
 */
public class DecimalColumnFormatter_FileLineObjectStub01 {

    private BigDecimal decimal01 = null;

    private BigDecimal decimal02 = null;

    private BigDecimal decimal03 = null;

    @SuppressWarnings("unused")
    private BigDecimal decimal04 = null;

    @SuppressWarnings("unused")
    private BigDecimal decimal05 = null;

    private BigDecimal decimal06 = null;

    private BigDecimal decimal07 = null;

    private BigDecimal decimal08 = null;

    private BigDecimal decimal09 = null;

    /**
     * @param decimal01 設定する decimal01
     */
    public void setDecimal01(BigDecimal decimal01) {
        this.decimal01 = decimal01;
    }

    /**
     * @param decimal02 設定する decimal02
     */
    public void setDecimal02(BigDecimal decimal02) {
        this.decimal02 = decimal02;
    }

    /**
     * @param decimal03 設定する decimal03
     */
    public void setDecimal03(BigDecimal decimal03) {
        this.decimal03 = decimal03;
    }

    /**
     * @param decimal04 設定する decimal04
     */
    public void setDecimal04(BigDecimal decimal04) {
        this.decimal04 = decimal04;
    }

    /**
     * @param decimal05 設定する decimal05
     */
    public void setDecimal05(BigDecimal decimal05) {
        this.decimal05 = decimal05;
    }

    /**
     * @param decimal06 設定する decimal06
     */
    public void setDecimal06(BigDecimal decimal06) {
        this.decimal06 = decimal06;
    }

    /**
     * @param decimal07 設定する decimal07
     */
    public void setDecimal07(BigDecimal decimal07) {
        this.decimal07 = decimal07;
    }

    /**
     * @param decimal08 設定する decimal08
     */
    public void setDecimal08(BigDecimal decimal08) {
        this.decimal08 = decimal08;
    }

    /**
     * @param decimal09 設定する decimal09
     */
    public void setDecimal09(BigDecimal decimal09) {
        this.decimal09 = decimal09;
    }

    /**
     * publicで引数なしの正常のgetterメソッド。
     * @return decimal01 設定値
     */
    public BigDecimal getDecimal01() {
        return decimal01;
    }

    /**
     * publicで引数なしの正常のgetterメソッド。
     * @return decimal02 設定値
     */
    public BigDecimal getDecimal02() {
        return decimal02;
    }

    /**
     * privateで引数なしの異常のgetterメソッド。
     * @return decimal03 設定値
     */
    @SuppressWarnings("unused")
    private BigDecimal getDecimal03() {
        return decimal03;
    }

    /**
     * publicで引数なしの異常のgetterメソッド。
     * @return decimal04 設定値
     */
    public BigDecimal getDecimal04() {
        throw new RuntimeException("getDecimal04のメッソドの例外発生");
    }

    /**
     * publicで引数ありの異常のgetterメソッド。
     * @return decimal05 設定値
     */
    public BigDecimal getDecimal05(BigDecimal decimal05) {
        return decimal05;
    }

    /**
     * publicで引数なしの正常のgetterメソッド。
     * @return decimal06 設定値
     */
    public BigDecimal getDecimal06() {
        return decimal06;
    }

    /**
     * publicで引数なしの正常のgetterメソッド。
     * @return decimal07 設定値
     */
    public BigDecimal getDecimal07() {
        return decimal07;
    }

    /**
     * publicで引数なしの正常のgetterメソッド。
     * @return decimal08
     */
    public BigDecimal getDecimal08() {
        return decimal08;
    }

    /**
     * publicで引数なしの正常のgetterメソッド。
     * @return decimal09
     */
    public BigDecimal getDecimal09() {
        return decimal09;
    }

}
