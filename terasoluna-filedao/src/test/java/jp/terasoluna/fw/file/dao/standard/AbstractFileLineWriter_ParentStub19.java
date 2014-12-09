package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.OutputFileColumn;

/**
 * AbstractFileLineWriterの試験で利用するファイル行オブジェクトの親スタブクラス。<br>
 * <br>
 * <code>@OutputFileColumn設定ありのフィールドを持つ<br>
 * <ul>
 * <li>フィールド：String column1
 *     <code>@OutputFileColumn</code>設定<br>
 * > columnIndex：0<br>
 * > その他項目：デフォルト値</li> </ul>
 * @author 趙俸徹
 */
public class AbstractFileLineWriter_ParentStub19 {

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

}
