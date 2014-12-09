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
 * </ul>
 * <br>
 * <code>@OutputFileColumn設定ありのフィールドを持つ<br>
 * <ul>
 * <li>フィールド：String column1
 *     <code>@OutputFileColumn</code>設定<br>
 * > columnIndex：0<br>
 * > その他項目：デフォルト値</li> </ul>
 * @author 趙俸徹
 */
@FileFormat()
public class AbstractFileLineWriter_Stub12 {

    /**
     * noMappingColumn1
     */
    private String noMappingColumn1 = null;

    /**
     * noMappingColumn2
     */
    private String noMappingColumn2 = null;

    /**
     * column1
     */
    @OutputFileColumn(columnIndex = 0)
    private String column1 = null;

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
