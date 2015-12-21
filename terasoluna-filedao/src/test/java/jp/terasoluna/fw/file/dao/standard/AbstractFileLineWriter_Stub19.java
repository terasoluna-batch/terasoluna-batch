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
 * <code>@OutputFileColumn設定ありのフィールドを持つ<br>
 * <ul>
 * <li>フィールド：String column2
 *     <code>@OutputFileColumn</code>設定<br>
 * > columnIndex：1<br>
 * > その他項目：デフォルト値</li> </ul> <code>@OutputFileColumn設定ありのフィールドを持つ<br>
 * <ul>
 * <li>フィールド：String column3
 *     <code>@OutputFileColumn</code>設定<br>
 * > columnIndex：2<br>
 * > その他項目：デフォルト値</li> </ul> <br>
 * ※親クラスも<code>@OutputFileColumn</code>設定ありのフィールドを持つ
 */
@FileFormat()
public class AbstractFileLineWriter_Stub19 extends
                                          AbstractFileLineWriter_ParentStub19 {

    /**
     * noMappingColumn1
     */
    private String noMappingColumn1 = null;

    /**
     * noMappingColumn2
     */
    private String noMappingColumn2 = null;

    /**
     * column2
     */
    @OutputFileColumn(columnIndex = 1)
    private String column2 = null;

    /**
     * column2
     */
    @OutputFileColumn(columnIndex = 2)
    private String column3 = null;

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
     * column3を取得する。
     * @return column3
     */
    public String getColumn3() {
        return column3;
    }

    /**
     * column3を設定する。
     * @param column3 column3
     */
    public void setColumn3(String column3) {
        this.column3 = column3;
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

}
