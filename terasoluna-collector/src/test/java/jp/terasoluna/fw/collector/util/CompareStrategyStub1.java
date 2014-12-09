package jp.terasoluna.fw.collector.util;

import jp.terasoluna.fw.collector.util.strategy.CompareStrategy;

public class CompareStrategyStub1 implements CompareStrategy<Object> {

    public boolean equalsObjects(Object value1, Object value2) {

        return value1.equals(value2);

    }

}
