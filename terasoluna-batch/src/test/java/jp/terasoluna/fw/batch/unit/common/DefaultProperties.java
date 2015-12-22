package jp.terasoluna.fw.batch.unit.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.terasoluna.fw.batch.unit.exception.UTRuntimeException;
import jp.terasoluna.fw.batch.unit.util.ClassLoaderUtils;

import org.springframework.util.Assert;

/**
 * UTライブラリで使用するデフォルトプロパティを管理するクラスです。
 * 
 * <pre>
 * プロパティはterasoluna-unit.propertiesに設定されています。
 * 環境にあわせて適宜変更してください。
 * </pre>
 */
public class DefaultProperties {
    /**
     * UTライブラリで使用するプロパティファイル
     */
    private static final String DEFAULT_FILE_PATH = "terasoluna-unit.properties";

    private static final ConcurrentMap<String, String> properties = new ConcurrentHashMap<String, String>();

    static {
        // プロパティファイルからキー・値を読み込みます。
        Properties defaults = new Properties();
        loadProperties(defaults, DEFAULT_FILE_PATH);
        for (Entry<?, ?> e : defaults.entrySet()) {
            properties.put((String) e.getKey(), (String) e.getValue());
        }
    }

    /**
     * プロパティファイルを読み込みます。
     * 
     * <pre>
     * プロパティファイルが存在しない場合は読み込みません。
     * 読み込み中にIO例外が発生した場合は{@link UTRuntimeException}をスローします。
     * </pre>
     * 
     * @param props プロパティ
     * @param filePath 読み込むプロパティファイル
     */
    private static void loadProperties(Properties props, String filePath)
                                                                         throws UTRuntimeException {
        Assert.notNull(props);
        Assert.notNull(filePath);
        ClassLoader cl = ClassLoaderUtils.getClassLoader();
        if (cl != null) {
            InputStream strm = cl.getResourceAsStream(filePath);
            try {
                if (strm != null) {
                    try {
                        props.load(strm);
                    } catch (IOException e) {
                        throw new UTRuntimeException(e);
                    }
                }
            } finally {
                if (strm != null) {
                    try {
                        strm.close();
                    } catch (IOException e) {
                        throw new UTRuntimeException(e);
                    }
                }
            }
        }
    }

    /**
     * プロパティ値を返却します。
     * 
     * @param key プロパティのキー
     * @return プロパティの値
     */
    public static String getValue(String key) {
        return properties.get(key);
    }

    /**
     * プロパティ値を返却します。
     * 
     * @param key プロパティのキー({@link PropertyKeys}形式)
     * @return プロパティの値
     */
    public static String getValue(PropertyKeys key) {
        return getValue(key.getKey());
    }
}
