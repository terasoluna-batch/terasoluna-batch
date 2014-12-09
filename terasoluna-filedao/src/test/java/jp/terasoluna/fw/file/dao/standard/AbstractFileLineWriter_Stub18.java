package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.FileFormat;
import jp.terasoluna.fw.file.annotation.OutputFileColumn;

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
 * <br>
 * <code>@OutputFileColumn設定ありのフィールドを持つ<br>
 * <ul>
 * <li>フィールド：String column1
 *     <code>@OutputFileColumn</code>設定<br>
 * > columnIndex：1(0が欠番である。)<br>
 * > その他項目：デフォルト値</li> </ul>
 * <ul>
 * <li>フィールド：String column2 <code>@OutputFileColumn</code>設定<br>
 * > columnIndex：2(0が欠番である。)<br>
 * > その他項目：デフォルト値</li>
 * </ul>
 * @author 趙俸徹
 */
@FileFormat()
public class AbstractFileLineWriter_Stub18 {

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
     * column1
     */
    @OutputFileColumn(columnIndex = 1)
    private String column1 = null;

    /**
     * column2
     */
    @OutputFileColumn(columnIndex = 2)
    private String column2 = null;

    /**
     * column1を取得する。
     * @return column1
     */
    public String getColumn1() {
        return column1;
    }

    /**
     * column1を設定する。
     * @param column1 column1
     */
    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    /**
     * column2を取得する。
     * @return column2
     */
    public String getColumn2() {
        return column2;
    }

    /**
     * column2を設定する。
     * @param column2 column2
     */
    public void setColumn2(String column2) {
        this.column2 = column2;
    }

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
