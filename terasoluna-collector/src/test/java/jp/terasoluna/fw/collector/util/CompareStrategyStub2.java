package jp.terasoluna.fw.collector.util;

import jp.terasoluna.fw.collector.util.strategy.CompareStrategy;

public class CompareStrategyStub2 implements CompareStrategy<String> {

    public boolean equalsObjects(String value1, String value2) {

        if (value1 != null && value2 != null) {
            // •¶Žš—ñ‚ÌŒã‚ë2•¶Žš‚ð”äŠr‚·‚é
            String substr1 = value1.substring(value1.length() - 2);
            String substr2 = value2.substring(value2.length() - 2);

            return substr1.equals(substr2);

        } else if (value1 == null && value2 == null) {
            return true;
        } else {
            return false;
        }

    }
}
