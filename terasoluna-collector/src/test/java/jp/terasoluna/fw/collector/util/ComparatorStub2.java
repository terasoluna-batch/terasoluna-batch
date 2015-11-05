package jp.terasoluna.fw.collector.util;

import java.util.Comparator;

import org.springframework.util.Assert;

@SuppressWarnings("unchecked")
public class ComparatorStub2 implements Comparator<Object> {

    public int compare(Object o1, Object o2) {
        if ((o1 == null && o2 != null) || (o1 != null && o2 == null)) {
            return -1;
        }
        if (o1 == null && o2 == null) {
            return 0;
        }
        Assert.isTrue(o1 instanceof Comparable,
                "The first object provided is not Comparable");
        Assert.isTrue(o2 instanceof Comparable,
                "The second object provided is not Comparable");
        return ((Comparable<Object>) o1).compareTo(o2);

    }

}
