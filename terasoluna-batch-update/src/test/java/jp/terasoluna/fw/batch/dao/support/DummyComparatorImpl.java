package jp.terasoluna.fw.batch.dao.support;

import java.util.Comparator;

public class DummyComparatorImpl implements Comparator<String> {

    public int compare(String o1, String o2) {
        return o2.compareTo(o1);
    }

}
