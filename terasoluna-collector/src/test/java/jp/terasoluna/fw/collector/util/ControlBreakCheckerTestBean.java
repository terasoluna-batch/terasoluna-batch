package jp.terasoluna.fw.collector.util;

/**
 * コントロールブレイクとコレクタの結合試験用のJavaBean
 *
 */
public class ControlBreakCheckerTestBean {

    public ControlBreakCheckerTestBean() {
    }

    public ControlBreakCheckerTestBean(String column1, String column2, String column3) {
        this.column1 = column1;
        this.column2 = column2;
        this.column3 = column3;
    }

    /**
     * コントロールブレイクキー
     */
    private String column1 = null;

    /**
     * 連番。
     */
    private String column2 = null;

    /**
     * 入力エラー対象。
     * "Exception"の時入力エラーになり、"validateError"のとき入力チェックエラーになる
     */
    private String column3 = null;

    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public String getColumn2() {
        return column2;
    }

    public void setColumn2(String column2) {
        this.column2 = column2;
    }

    public String getColumn3() {
        return column3;
    }

    public void setColumn3(String column3) {
        this.column3 = column3;
    }

}
