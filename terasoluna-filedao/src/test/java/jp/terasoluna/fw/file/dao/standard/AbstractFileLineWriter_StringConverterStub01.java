package jp.terasoluna.fw.file.dao.standard;

import jp.terasoluna.fw.file.annotation.StringConverter;

/**
 * デフォルトコンストラクタを持たないStringConverterスタブ
 * @author 趙俸徹
 */
public class AbstractFileLineWriter_StringConverterStub01 implements
                                                         StringConverter {

    /**
     * コンストラクタ
     */
    public AbstractFileLineWriter_StringConverterStub01(String dummy) {
    }

    /**
     * 変換処理を行わない。
     * @param s
     * @return
     */
    public String convert(String s) {
        return s;
    }

}
