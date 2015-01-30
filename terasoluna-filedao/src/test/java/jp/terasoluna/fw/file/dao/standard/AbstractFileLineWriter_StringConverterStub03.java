package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.StringConverter;

/**
 * StringConverterスタブクラス。<br>
 * 入力データに"_convert()"を追加した結果を返す
 * @author 趙俸徹
 */
public class AbstractFileLineWriter_StringConverterStub03 implements
                                                         StringConverter {

    /**
     * コンストラクタ
     */
    public AbstractFileLineWriter_StringConverterStub03() {
    }

    /**
     * 変換処理を行わない。
     */
    public String convert(String s) {
        return s + "_convert()";
    }
}
