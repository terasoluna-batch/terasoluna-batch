package jp.terasoluna.fw.collector.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import jp.terasoluna.fw.collector.util.strategy.CompareStrategy;

/**
 * DateをHourの精度で比較するCompareStrategy。
 * (このスタブは、スレッド間で共有してはならない。)
 */
public class CompareStrategyStub3 implements CompareStrategy<Date> {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH");

    public boolean equalsObjects(Date value1, Date value2) {
        return sdf.format(value1).equals(sdf.format(value2));
    }
}
