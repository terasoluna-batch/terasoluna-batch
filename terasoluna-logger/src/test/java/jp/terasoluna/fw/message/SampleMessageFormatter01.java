package jp.terasoluna.fw.message;

import java.util.Arrays;

public class SampleMessageFormatter01 implements MessageFormatter {

    public String format(String pattern, Object... args) {
        return pattern + " " + Arrays.toString(args);
    }

}
