package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.StringConverter;

/**
 * privateデフォルトコンストラクタを持つStringConverterスタブ
 * @author 趙俸徹
 */
public class AbstractFileLineWriter_StringConverterStub02 implements
                                                         StringConverter {

    /**
     * コンストラクタ
     */
    private AbstractFileLineWriter_StringConverterStub02() {
    }

    /**
     * 変換処理を行わない。
     */
    public String convert(String s) {
        return s;
    }
}
