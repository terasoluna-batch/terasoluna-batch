package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.FileFormat;

/**
 * AbstractFileLineWriterの試験で利用するファイル行オブジェクトのスタブクラス。<br>
 * <br>
 * 以下の@FileFormatの設定を持つ<br>
 * <ul>
 * <li>全項目：デフォルト値</li>
 * </ul>
 * <br>
 * <code>@OutputFileColumn</code>設定なしのフィールドを持つ<br>
 * <ul>
 * <li>フィールド：String noMappingColumn1</li>
 * <li>フィールド：String noMappingColumn2</li>
 * <li>フィールド：String noMappingColumn3</li>
 * </ul>
 * @author 趙俸徹
 */
@FileFormat()
public class AbstractFileLineWriter_Stub11 {

    /**
     * noMappingColumn1
     */
    private String noMappingColumn1 = null;

    /**
     * noMappingColumn2
     */
    private String noMappingColumn2 = null;

    /**
     * noMappingColumn3
     */
    private String noMappingColumn3 = null;

    /**
     * noMappingColumn1を取得する。
     * @return noMappingColumn1
     */
    public String getNoMappingColumn1() {
        return noMappingColumn1;
    }

    /**
     * noMappingColumn1を設定する。
     * @param noMappingColumn1 noMappingColumn1
     */
    public void setNoMappingColumn1(String noMappingColumn1) {
        this.noMappingColumn1 = noMappingColumn1;
    }

    /**
     * noMappingColumn2を取得する。
     * @return noMappingColumn2
     */
    public String getNoMappingColumn2() {
        return noMappingColumn2;
    }

    /**
     * noMappingColumn2を設定する。
     * @param noMappingColumn2 noMappingColumn2
     */
    public void setNoMappingColumn2(String noMappingColumn2) {
        this.noMappingColumn2 = noMappingColumn2;
    }

    /**
     * noMappingColumn3を取得する。
     * @return noMappingColumn3
     */
    public String getNoMappingColumn3() {
        return noMappingColumn3;
    }

    /**
     * noMappingColumn3を設定する。
     * @param noMappingColumn3 noMappingColumn3
     */
    public void setNoMappingColumn3(String noMappingColumn3) {
        this.noMappingColumn3 = noMappingColumn3;
    }

}
