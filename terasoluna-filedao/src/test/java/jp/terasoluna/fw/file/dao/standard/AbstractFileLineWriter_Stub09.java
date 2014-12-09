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
 * </ul>
 * @author 趙俸徹
 */
@FileFormat()
public class AbstractFileLineWriter_Stub09 {

    /**
     * noMappingColumn1
     */
    private String noMappingColumn1 = null;

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

}
