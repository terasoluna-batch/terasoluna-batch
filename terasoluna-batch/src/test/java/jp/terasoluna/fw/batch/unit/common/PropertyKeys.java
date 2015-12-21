package jp.terasoluna.fw.batch.unit.common;

/**
 * プロパティのキーを表現する列挙型です。
 * 
 * <pre>
 * 列挙型を小文字にし、「_」を「.」に置換したものがプロパティのキーになります。
 * 
 * AAA_BBB => aaa.bbb
 * 
 * この列挙型で管理するプロパティのキーに大文字を使用できないことに注意してください。
 * </pre>
 * 
 *
 */
public enum PropertyKeys {
    /**
     * コンテキストファイルのパス
     */
    CONTEXTFILE_DIR,
    /**
     * アプリケーションコンテキスト定義ファイル名
     */
    APPLICATIONCONTEXT_FILE;

    /**
     * プロパティのキー
     */
    private final String key;

    private PropertyKeys() {
        key = name().toLowerCase().replace("_", ".");
    }

    /**
     * プロパティのキー形式で返却します。
     * 
     * @return プロパティのキー
     */
    public String getKey() {
        return key;
    }
}
