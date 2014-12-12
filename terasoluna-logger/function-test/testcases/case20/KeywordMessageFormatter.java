package case20;

import java.text.MessageFormat;

import org.apache.log4j.MDC;

import jp.terasoluna.fw.message.MessageFormatter;

public class KeywordMessageFormatter implements MessageFormatter {

    public String format(String pattern, Object... args) {
        // 「キーワード,メッセージ本文」という形式を想定
        String[] vals = pattern.split(",");
        String pat = null;
        if (vals.length > 1) {
            // キーワードが設定されている場合
            String keyword = vals[0];
            pat = vals[1];
            // キーワードをMDCに設定する
            MDC.put("keyword", keyword);
        } else {
            pat = pattern;
        }
        return MessageFormat.format(pat, args);
    }
}
